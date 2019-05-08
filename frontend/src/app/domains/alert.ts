import { AlertType } from '../enums/alert-type.enum';

export class Alert {

    message: string;
    type: AlertType;

    constructor(message: string, type: AlertType) {
        this.message = message;
        this.type = type;
    }

}
