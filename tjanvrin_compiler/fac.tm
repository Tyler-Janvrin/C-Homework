Hello from visit!
FunctionDec: main
    NameTy:  void 
    CompoundExp: 
        SimpleDec: x
            NameTy:  int 
        SimpleDec: fac
            NameTy:  int 
        AssignExp:
            VarExp: 
                SimpleVar: x
            CallExp: input
        AssignExp:
            VarExp: 
                SimpleVar: fac
            IntExp: 1
        WhileExp: 
            OpExp: > 
                VarExp: 
                    SimpleVar: x
                IntExp: 1
            CompoundExp: 
                AssignExp:
                    VarExp: 
                        SimpleVar: fac
                    OpExp: * 
                        VarExp: 
                            SimpleVar: fac
                        VarExp: 
                            SimpleVar: x
                AssignExp:
                    VarExp: 
                        SimpleVar: x
                    OpExp: - 
                        VarExp: 
                            SimpleVar: x
                        IntExp: 1
        CallExp: output
            VarExp: 
                SimpleVar: fac
