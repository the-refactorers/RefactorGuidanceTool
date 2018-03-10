package Client;

import Server.BankPaymentController;
import Shared.IRemotePayment;

public class DirectPayment {

    IRemotePayment controller = null;

    public void processPayment()
    {
        controller.PayRemote("myAccount", "transferAccount", 12);
    }
}
