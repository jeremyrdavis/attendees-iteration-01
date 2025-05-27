package dddhexagonalworkshop.conference.attendees.domain.valueobjects;

public enum TShirtSize {
    XS, S, M, L, XL, XXL;

    public static TShirtSize fromString(String size) {
        try {
            return TShirtSize.valueOf(size.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid T-Shirt size: " + size);
        }
    }
}
