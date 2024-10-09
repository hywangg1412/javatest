package View;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class Menu<T> {
    protected ArrayList<T> mChon= new ArrayList();
    protected String title="";
//--------------------------------------

    public Menu(T[] mc, String td){
        for(T m:mc) mChon.add(m);
        title=td;
    }
    public Menu(ArrayList<T> mc, String td) {
        this.mChon = mc;
        title=td;
    }
    //--------------------------------------
    public void setTitle(String td){
        title=td;
    }
    //--------------------------------------
    public ArrayList<T> getmChon() {
        return mChon;
    }
    //--------------------------------------
    public void addItem(Object a){
        mChon.add((T) a);
    }
    //--------------------------------------
    public void display(){
        System.out.println(title);
        System.out.println("-----------------------------");
        for(int i=0; i<mChon.size();i++)
            System.out.println((i+1)+". "+mChon.get(i));
        System.out.println("-----------------------------");
    }
    //--------------------------------------
    public int getSelected(){
        display();
        System.out.print("Please select..: ");
        Scanner sc= new Scanner(System.in);
        return sc.nextInt();
    }
    //--------------------------------------
    public abstract void execute(int n);
    //--------------------------------------
    public void run() {
        while (true) {
            int n = getSelected();
            if (n > mChon.size()) {
                System.out.println("-> Invalid selection, please try again.");
                continue;
            }
            if (n == mChon.size()) break;
            execute(n);
        }
    }

//--------------------------------------
}
