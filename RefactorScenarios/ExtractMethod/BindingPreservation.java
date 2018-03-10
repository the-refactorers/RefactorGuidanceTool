public class BindingPreservation {



        class X { }

        class A {
            int p = 15;

            Object m(){
                @SuppressWarnings("rawtypes")
                int l = 9;
                int p = 6;

                while (p<10) {
                    if (p>10) return new X();
                    p++; l++;
                }
                this.p = p;

                // - IntelliJ checks if p is used later on, if this is not the case it assumes p can be changed locally in new method scope
                //   There is a possible risk here that it is assumed that p in the local scope is updated, while it is actually done in the local scope of the new method
                // - When refactoring with more than one parameter that is changing in the local scope; intellij is asking to perform the refactoring operation by introducing an inner class object which can hold the state

                //
//                if (p>11) return new X();       //<< EXTRACT
//                else while (p<10) {p++;l++;}    //<< EXTRACT
//
                System.out.println(l);
                System.out.println(p);

                return new X();
            }

            public void pp(){
                System.out.println(p);
            }

            private boolean test(int p) {
                if (p<10) return true;
                return false;
            }

            private void s()
            {
                int x = 0;

                // Refactoring of this statement will result in IntelliJ into
                // Introduction of an inner class which can store state. A call is made to determine if direct return is needed.
                // otherwise x new value of x is read.
                if (x>3) {
                    return;
                } else {
                    x = x + 2;
                }

                //if(refactoredMethod.result) retuern
                int i=0;
//                for(i=0; i<10; i++)
//                {
//                    if(i==0) {
//                        i=i+1;
//                        break;        // This break will cause the loop (innermost) to stop just after one iteration;
//                    }
//                }
//
//                while (i<100)
//                {
//                    if (i==55)
//                        continue;
//                    i++;
//                }

                x=x+1;
            }
        }

}
