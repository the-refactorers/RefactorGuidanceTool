package analysis;


// Kunnen we hier 'live' automatisch instellen, op basis van eerdere waarden?
public class VariableFacts {

        public boolean write;
        public boolean read;
        public boolean cond_write;
        public boolean live; // A variable is written which before only has been read in the section

}
