package www.bookstore.com.exception;

public enum EnumCode {

    Active(1), DeActive(0);

    private Integer value;


    EnumCode(Integer code) {

        this.value = code;
    }

    public Integer getValue() {

        return this.value;
    }
}
