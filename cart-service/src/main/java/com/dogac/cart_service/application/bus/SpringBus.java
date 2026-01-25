package com.dogac.cart_service.application.bus;

import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import com.dogac.cart_service.application.core.Command;
import com.dogac.cart_service.application.core.CommandHandler;
import com.dogac.cart_service.application.core.Query;
import com.dogac.cart_service.application.core.QueryHandler;

@Component
public class SpringBus implements CommandBus, QueryBus {

    private final ApplicationContext context;

    public SpringBus(ApplicationContext context) {
        this.context = context;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, C extends Command<R>> R send(C command) {
        // Command sınıfının implemente ettiği interface üzerinden R (Response) tipini
        // buluyoruz
        ResolvableType commandInterface = ResolvableType.forClass(command.getClass()).as(Command.class);
        Class<?> responseType = commandInterface.getGeneric(0).resolve();

        // Spring'den tam jenerik eşleşmesi olan Handler'ı istiyoruz
        String[] beanNames = context.getBeanNamesForType(
                ResolvableType.forClassWithGenerics(CommandHandler.class, command.getClass(), responseType));

        if (beanNames.length == 0) {
            throw new RuntimeException("Handler bulunamadı: " + command.getClass().getSimpleName() +
                    " için beklenen dönüş tipi: " + responseType.getSimpleName());
        }

        CommandHandler<C, R> handler = (CommandHandler<C, R>) context.getBean(beanNames[0]);
        return handler.handle(command);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, Q extends Query<R>> R execute(Q query) {
        // 1. Query interface'inden R (Response) tipini çalışma anında buluyoruz
        ResolvableType queryInterface = ResolvableType.forClass(query.getClass()).as(Query.class);
        Class<?> responseType = queryInterface.getGeneric(0).resolve();

        // 2. Spring Context içerisinde hem Query hem de Response tipiyle eşleşen Bean'i
        // arıyoruz
        String[] beanNames = context.getBeanNamesForType(
                ResolvableType.forClassWithGenerics(QueryHandler.class, query.getClass(), responseType));

        // 3. Güvenlik kontrolü: Bean bulunamazsa anlamlı bir hata fırlatıyoruz
        if (beanNames.length == 0) {
            throw new IllegalStateException("No QueryHandler found for " + query.getClass().getSimpleName() +
                    " with response type " + (responseType != null ? responseType.getSimpleName() : "Unknown"));
        }

        // 4. Bean'i alıp cast ediyoruz ve handle metodunu çağırıyoruz
        QueryHandler<Q, R> handler = (QueryHandler<Q, R>) context.getBean(beanNames[0]);
        return handler.handle(query);
    }
}
