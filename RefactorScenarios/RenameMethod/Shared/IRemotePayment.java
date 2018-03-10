package Shared;

/**
 * Through this interface payments can be made by clients to clients.
 * Changes to this interface are only allowed after consulting the architect of this package.
 *
 */
public interface IRemotePayment {
       void PayRemote(String sender, String receiver, double amount);
}
