package Client;

import Shared.IRemotePayment;

public class DirectPayment {

    IRemotePayment controller = null;

    public void processPayment()
    {
        controller.PayRemote("myAccount", "transferAccount", 12);
    }
}
