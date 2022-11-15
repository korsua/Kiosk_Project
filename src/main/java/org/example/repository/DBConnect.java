package org.example.repository;

public enum DBConnect {
    URL("jdbc:mariadb://localhost:3306/"),
    DB_ID("ROOT"),
    DB_PW("1234");
    private final String amount;

    DBConnect(final String amount) {
        this.amount = amount;
    }

}
