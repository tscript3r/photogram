import { AlertType } from '../enum/alert-type.enum';

export class Alert {
  text: string;
  type: AlertType;

  constructor(text = '', type = AlertType.SUCCESS) {
    this.text = text;
    this.type = type;
  }
}
