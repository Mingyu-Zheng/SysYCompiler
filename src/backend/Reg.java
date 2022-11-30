package backend;

public class Reg {
    public static final Reg ZERO = new Reg("0",0);
    public static final Reg AT = new Reg("at",1);
    public static final Reg V0 = new Reg("v0",2);

    public static final Reg RA = new Reg("ra",3);
    public static final Reg SP = new Reg("sp",4);
    public static final Reg AR = new Reg("a0",5);

    public static final Reg T0 = new Reg("t0",6);
    public static final Reg T1 = new Reg("t1",7);
    public static final Reg T2 = new Reg("t2",8);
    public static final Reg T3 = new Reg("t3",9);
    public static final Reg T4 = new Reg("t4",10);
    public static final Reg T5 = new Reg("t5",11);
    public static final Reg T6 = new Reg("t6",12);
    public static final Reg T7 = new Reg("t7",13);
    public static final Reg T8 = new Reg("t8",14);
    public static final Reg T9 = new Reg("t9",15);
    public static final Reg S0 = new Reg("s0",16);
    public static final Reg S1 = new Reg("s1",17);
    public static final Reg S2 = new Reg("s2",18);
    public static final Reg S3 = new Reg("s3",19);
    public static final Reg S4 = new Reg("s4",20);
    public static final Reg S5 = new Reg("s5",21);
    public static final Reg S6 = new Reg("s6",22);
    public static final Reg S7 = new Reg("s7",23);
    public static final Reg S8 = new Reg("gp",24);
    public static final Reg S9 = new Reg("k0",25);
    public static final Reg S10 = new Reg("k1",26);
    public static final Reg S11 = new Reg("a1",27);
    public static final Reg S12 = new Reg("a2",28);
    public static final Reg S13 = new Reg("a3",29);
    public static final Reg S14 = new Reg("v1",30);
    public static final Reg CT = new Reg("fp",31); // Compiler Temp

    public final String name;
    public final int index;

    private Reg(String name, int index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public String toString() {
        return "$" + name;
    }
}
