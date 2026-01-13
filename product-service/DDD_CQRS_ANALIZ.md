# DDD + CQRS Analiz Raporu - Product Service

## 📋 Genel Bakış

Bu dokümanda, product-service projesinde DDD (Domain-Driven Design) ve CQRS (Command Query Responsibility Segregation) prensiplerine göre iyileştirilmesi gereken alanlar belirlenmiştir.

---

## 🔴 Kritik Sorunlar

### 1. **Domain Service'in Sorumlulukları ve Persistence İşlemleri**

**Sorun:** `ProductDomainService` sınıfı `ProductRepository` interface'ine bağımlı (bu teknik olarak doğru - DIP'e uygun), ancak Domain Service içinde repository üzerinden **persistence işlemleri** yapılıyor. Domain Service'ler persistence işlemlerini yapmamalıdır.

**Konum:** `domain/services/ProductDomainService.java`

**DDD Prensibi İhlali:**

- Domain Service'ler **domain mantığını** içermeli, **persistence işlemlerini** yapmamalıdır
- `reserveStock()`, `releaseStock()` gibi metodlar repository'den çekip kaydediyor - bu işlemler Application Layer'da olmalı
- `validateProductCreation()` gibi uniqueness kontrolü Domain Service'te değil, Application Layer'da yapılmalı
- Domain Service'ler genellikle **birden fazla aggregate'i koordine eder** veya **tek bir aggregate'in içinde olmayan domain kurallarını** uygular

**Not:** `ProductRepository` bir interface olduğu için teknik olarak doğru, ancak Domain Service'in repository üzerinden persistence yapması sorunlu.

**Öneri:**

- Domain Service'ten repository üzerinden persistence işlemlerini kaldırın
- Repository erişimlerini ve persistence işlemlerini Application Service (Command Handler) içine taşıyın
- Domain Service sadece domain kurallarını ve business logic'i içersin
- Uniqueness kontrolü gibi cross-cutting concern'ler Application Layer'da yapılmalı

**Örnek Düzeltme:**

```java
// ❌ YANLIŞ - Domain Service'te persistence işlemleri
public class ProductDomainService {
    private final ProductRepository productRepository; // Interface - teknik olarak doğru

    public void validateProductCreation(Product product) {
        // Uniqueness kontrolü Domain Service'te yapılıyor
        if (productRepository.existsByName(product.getName().value())) {
            throw new DuplicateProductException(...);
        }
    }

    public void reserveStock(ProductId productId, StockQuantity quantity) {
        // Repository'den çekip kaydediyor - persistence işlemi
        Product product = productRepository.findById(productId)...
        product.decreaseStock(quantity);
        productRepository.save(product); // ❌ Domain Service persistence yapmamalı
    }
}

// ✅ DOĞRU - Persistence Application Layer'da
public class ProductDomainService {
    // Repository bağımlılığı yok - sadece domain mantığı

    public void validateStockReservation(Product product, StockQuantity quantity) {
        // Sadece domain kuralını kontrol eder
        if (!product.hasEnoughStock(quantity.value())) {
            throw new InsufficientStockException(...);
        }
    }

    public void validatePriceChange(Money newPrice) {
        // Sadece domain kuralını kontrol eder
        if (newPrice.amount().intValue() <= 0) {
            throw new InvalidPriceChangeException("New Price cannot be 0");
        }
    }
}

public class ReserveStockCommandHandler {
    private final ProductRepository productRepository;
    private final ProductDomainService productDomainService;

    public void handle(ReserveStockCommand command) {
        // Persistence işlemleri Application Layer'da
        Product product = productRepository.findById(command.productId())
            .orElseThrow(() -> new ProductNotFoundException(...));

        // Domain Service sadece domain kuralını kontrol eder
        productDomainService.validateStockReservation(product, command.quantity());

        // Domain işlemi aggregate üzerinden yapılır
        product.decreaseStock(command.quantity());

        // Persistence Application Layer'da
        productRepository.save(product);
    }
}

public class CreateProductCommandHandler {
    private final ProductRepository productRepository;
    private final ProductDomainService productDomainService;

    public CreatedProductResponse handle(CreateProductCommand command) {
        // Uniqueness kontrolü Application Layer'da
        if (productRepository.existsByName(command.productName())) {
            throw new DuplicateProductException(...);
        }

        // Domain Service sadece domain mantığını içerir (eğer gerekiyorsa)
        Product product = Product.create(...); // Veya factory method

        // Persistence Application Layer'da
        productRepository.save(product);
        return createProductMapper.toResponse(product);
    }
}
```

---

### 2. **CQRS: Query Handler'lar Domain Entity'lerini Kullanıyor**

**Sorun:** Query Handler'lar (`GetProductByIdQueryHandler`, `GetProductListQueryHandler`) domain entity'lerini (`Product`) doğrudan kullanıyor ve bunları DTO'ya map ediyor.

**Konum:** `application/queryHandlers/GetProductByIdQueryHandler.java`, `GetProductListQueryHandler.java`

**CQRS Prensibi İhlali:** CQRS pattern'inde Query tarafı için ayrı read model'ler kullanılmalıdır. Command tarafı domain entity'lerini kullanırken, Query tarafı optimized read model'leri kullanmalıdır.

**Öneri:**

- Query'ler için ayrı read model entity'leri oluşturun (`ProductReadModel`, `ProductView`)
- Query Handler'lar bu read model'leri kullanmalı
- Read model'ler doğrudan database'den projection ile çekilebilir

**Örnek Düzeltme:**

```java
// ✅ DOĞRU - Read Model kullanımı
public interface ProductReadRepository {
    Optional<ProductView> findById(UUID id);
    List<ProductView> findAll();
}

public record ProductView(
    UUID id,
    String productName,
    String productDescription,
    BigDecimal amount,
    Currency currency,
    Integer stockQuantity
) {}

@Component
public class GetProductByIdQueryHandler implements QueryHandler<GetProductByIdQuery, ProductResponse> {
    private final ProductReadRepository productReadRepository; // Read model repository

    @Override
    public ProductResponse handle(GetProductByIdQuery query) {
        ProductView view = productReadRepository.findById(query.id())
            .orElseThrow(() -> new ProductNotFoundException(...));
        return ProductResponseMapper.toResponse(view);
    }
}
```

---

### 3. **Mediator/Dispatcher Pattern Eksikliği**

**Sorun:** Controller doğrudan Command/Query Handler'lara bağımlı. Her handler'ı manuel olarak inject ediyor.

**Konum:** `web/ProductContoller.java`

**CQRS Prensibi İhlali:** CQRS pattern'inde genellikle bir mediator/dispatcher kullanılır. Bu, controller'ın handler'lara doğrudan bağımlı olmasını önler ve daha esnek bir yapı sağlar.

**Öneri:**

- `CommandBus` ve `QueryBus` interface'leri oluşturun
- Handler'ları otomatik olarak bulup dispatch eden bir implementasyon yapın
- Controller sadece bus'ları kullansın

**Örnek Düzeltme:**

```java
// ✅ DOĞRU - Bus pattern kullanımı
public interface CommandBus {
    <R> R execute(Command<R> command);
}

public interface QueryBus {
    <R> R execute(Query<R> query);
}

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping
    public ResponseEntity<CreatedProductResponse> createProduct(
            @Valid @RequestBody CreateProductCommand command) {
        CreatedProductResponse response = commandBus.execute(command);
        return ResponseEntity.created(URI.create("/products/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        ProductResponse response = queryBus.execute(new GetProductByIdQuery(id));
        return ResponseEntity.ok(response);
    }
}
```

---

### 4. **Unit of Work Pattern Eksikliği**

**Sorun:** Transaction yönetimi sadece `@Transactional` annotation ile yapılıyor. Unit of Work pattern'i kullanılmıyor.

**Konum:** Tüm Command Handler'lar

**DDD Prensibi İhlali:** Aggregate'lerin tutarlılığını sağlamak için Unit of Work pattern'i kullanılmalıdır. Bu, transaction yönetimini daha açık ve kontrol edilebilir hale getirir.

**Öneri:**

- `UnitOfWork` interface'i oluşturun
- Command Handler'larda UnitOfWork kullanın
- Transaction boundary'leri açıkça belirleyin

**Örnek Düzeltme:**

```java
public interface UnitOfWork {
    void begin();
    void commit();
    void rollback();
    <T> T execute(Supplier<T> operation);
}

@Component
public class CreateProductCommandHandler implements CommandHandler<CreateProductCommand, CreatedProductResponse> {
    private final UnitOfWork unitOfWork;
    private final ProductRepository productRepository;

    @Override
    public CreatedProductResponse handle(CreateProductCommand command) {
        return unitOfWork.execute(() -> {
            // Transaction içinde çalışan kod
            Product product = productDomainService.createProduct(...);
            productRepository.save(product);
            return createProductMapper.toResponse(product);
        });
    }
}
```

---

### 5. **Domain Events Eksikliği**

**Sorun:** Domain event'leri yok. Aggregate'lerdeki değişiklikler event olarak yayınlanmıyor.

**Konum:** `domain/entities/Product.java`

**DDD Prensibi İhlali:** Domain event'leri, aggregate'lerdeki önemli değişiklikleri diğer bounded context'lere veya application layer'a bildirmek için kullanılır.

**Öneri:**

- `DomainEvent` interface'i oluşturun
- `Product` aggregate'inde event'leri toplayın
- Command Handler'larda event'leri publish edin

**Örnek Düzeltme:**

```java
public interface DomainEvent {
    UUID eventId();
    Instant occurredOn();
}

public record ProductCreatedEvent(
    UUID eventId,
    Instant occurredOn,
    UUID productId,
    String productName
) implements DomainEvent {
    public ProductCreatedEvent(UUID productId, String productName) {
        this(UUID.randomUUID(), Instant.now(), productId, productName);
    }
}

public class Product {
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public static Product create(...) {
        Product product = new Product(...);
        product.domainEvents.add(new ProductCreatedEvent(product.id.value(), product.name.value()));
        return product;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
```

---

## 🟡 Orta Öncelikli Sorunlar

### 6. **Aggregate Root Koruma Eksikliği**

**Sorun:** `Product` entity'si aggregate root olarak işaretlenmemiş ve korunmamış. Repository'den dönen Product'lar doğrudan manipüle edilebiliyor.

**Konum:** `domain/entities/Product.java`

**DDD Prensibi İhlali:** Aggregate root'lar, aggregate içindeki tüm değişikliklerin tek giriş noktası olmalıdır.

**Öneri:**

- `Product` sınıfını `AggregateRoot` interface'i ile işaretleyin
- Aggregate içindeki değişikliklerin sadece aggregate root üzerinden yapılmasını sağlayın
- Business logic'i aggregate içinde tutun

---

### 7. **Specification Pattern Eksikliği**

**Sorun:** Repository'de `existsByName` gibi query method'ları var ama bunlar domain specification pattern'i kullanmıyor.

**Konum:** `domain/repositories/ProductRepository.java`

**DDD Prensibi İhlali:** Complex query'ler için Specification pattern kullanılmalıdır.

**Öneri:**

- `Specification<T>` interface'i oluşturun
- Query'leri specification'lara dönüştürün

**Örnek:**

```java
public interface Specification<T> {
    boolean isSatisfiedBy(T entity);
}

public class ProductNameSpecification implements Specification<Product> {
    private final String name;

    @Override
    public boolean isSatisfiedBy(Product product) {
        return product.getName().value().equals(name);
    }
}
```

---

### 8. **Value Object Immutability Sorunları**

**Sorun:** Value object'ler (`Money`, `ProductName`, vb.) record olarak tanımlanmış (iyi) ama bazılarında `@Embeddable` annotation var ki bu JPA bağımlılığı oluşturuyor.

**Konum:** `domain/valueobjects/Money.java`

**DDD Prensibi İhlali:** Domain katmanı infrastructure (JPA) bağımlılıkları içermemelidir.

**Öneri:**

- Value object'lerden JPA annotation'larını kaldırın
- Mapping işlemlerini infrastructure katmanında yapın

---

### 9. **Application Service Katmanı Belirsizliği**

**Sorun:** Command Handler'lar ve Query Handler'lar `@Component` olarak işaretlenmiş ama bunlar Application Service olarak düşünülmeli.

**Konum:** Tüm Handler'lar

**DDD Prensibi İhlali:** Application Service'ler, use case'leri koordine eder ve domain service'leri kullanır.

**Öneri:**

- Handler'ları Application Service olarak düşünün
- Naming convention'ı netleştirin (örn: `CreateProductApplicationService`)

---

### 10. **Error Handling ve Validation Stratejisi**

**Sorun:** Validation hem Command'da (`@Valid`, `@NotBlank`) hem de Value Object'lerde yapılıyor. Bu duplicate validation'a neden olabilir.

**Konum:** Command'lar ve Value Object'ler

**Öneri:**

- Validation stratejisini netleştirin
- Command validation'ı input validation için kullanın
- Value Object validation'ı domain invariants için kullanın
- Result pattern kullanarak error handling'i iyileştirin

**Örnek:**

```java
public sealed interface Result<T> {
    record Success<T>(T value) implements Result<T> {}
    record Failure<T>(List<String> errors) implements Result<T> {}
}

public class CreateProductCommandHandler {
    public Result<CreatedProductResponse> handle(CreateProductCommand command) {
        // Validation ve business logic
        if (errors.isEmpty()) {
            return new Result.Success<>(response);
        } else {
            return new Result.Failure<>(errors);
        }
    }
}
```

---

## 🟢 Düşük Öncelikli İyileştirmeler

### 11. **Controller İsimlendirme Hatası**

**Sorun:** `ProductContoller` yazım hatası var (Controller olmalı).

**Konum:** `web/ProductContoller.java`

**Öneri:** `ProductController` olarak düzeltin.

---

### 12. **Delete Handler Return Type**

**Sorun:** `DeleteProductCommandHandler` `Void` döndürüyor ve `null` return ediyor.

**Konum:** `application/commandHandlers/DeleteProductCommandHandler.java`

**Öneri:**

- `Void` yerine `void` kullanın veya
- `DeleteProductResponse` DTO'su oluşturun

---

### 13. **Update Handler'da Response Dönmüyor**

**Sorun:** `UpdateProductCommandHandler` response oluşturuyor ama controller'da kullanılmıyor.

**Konum:** `web/ProductContoller.java:93`

**Öneri:** Response'u kullanın veya `void` döndürün.

---

### 14. **Missing Pagination**

**Sorun:** `GetProductListQuery` pagination desteği yok.

**Öneri:** Pagination parametreleri ekleyin.

---

### 15. **Missing Caching Strategy**

**Sorun:** Query'ler için caching stratejisi yok.

**Öneri:** Read model'ler için caching ekleyin.

---

## 📊 Öncelik Matrisi

| Öncelik   | Sorun                                      | Etki   | Zorluk    |
| --------- | ------------------------------------------ | ------ | --------- |
| 🔴 Kritik | Domain Service Repository Bağımlılığı      | Yüksek | Orta      |
| 🔴 Kritik | Query Handler'lar Domain Entity Kullanıyor | Yüksek | Orta      |
| 🔴 Kritik | Mediator Pattern Eksikliği                 | Orta   | Düşük     |
| 🔴 Kritik | Unit of Work Eksikliği                     | Yüksek | Orta      |
| 🔴 Kritik | Domain Events Eksikliği                    | Yüksek | Yüksek    |
| 🟡 Orta   | Aggregate Root Koruma                      | Orta   | Düşük     |
| 🟡 Orta   | Specification Pattern                      | Düşük  | Orta      |
| 🟡 Orta   | Value Object JPA Bağımlılığı               | Orta   | Düşük     |
| 🟢 Düşük  | Controller İsimlendirme                    | Düşük  | Çok Düşük |
| 🟢 Düşük  | Delete Handler Return Type                 | Düşük  | Çok Düşük |

---

## 🎯 Önerilen Uygulama Sırası

1. **Adım 1:** Domain Service'ten repository bağımlılığını kaldırın
2. **Adım 2:** Read Model'ler için ayrı repository'ler oluşturun
3. **Adım 3:** CommandBus ve QueryBus implementasyonu ekleyin
4. **Adım 4:** Unit of Work pattern'i ekleyin
5. **Adım 5:** Domain Events ekleyin
6. **Adım 6:** Diğer iyileştirmeleri uygulayın

---

## 📚 Referanslar

- **DDD:** Domain-Driven Design - Eric Evans
- **CQRS:** Command Query Responsibility Segregation - Greg Young
- **Implementing Domain-Driven Design:** Vaughn Vernon
