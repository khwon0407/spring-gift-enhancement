package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class WrongPriceException extends BadRequestException {
    public WrongPriceException() {
        super("가격은 0보다 커야 합니다.");
    }
}
