package pl.exsio.querydsl.entityql.config.dto

class OrderItemBookCountDto {

    private final Long orderId;

    private final Long bookCount;

    OrderItemBookCountDto(Long orderId, Long bookCount) {
        this.orderId = orderId
        this.bookCount = bookCount
    }

    Long getOrderId() {
        return orderId
    }

    Long getBookCount() {
        return bookCount
    }
}
