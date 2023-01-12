# 编译设计文档

短短几个月的时间从对编译知之甚少到自己写出了一个真的可以生成汇编代码的编译器，内心的震撼和收获还是很大的，遗憾的是由于时间和能力所限，自己此前对编译器的完善和优化方面的一些想法没有完全地付诸实践，效果上比较平庸，最后的代码也没有重构到比较满意的结构，本文档对当前我所实现的编译器的结构进行了简要的说明，结构和设计都不免有冗杂的地方，还请斧正。

### 一、编译器总体设计

#### 总体结构

编译器总体结构大致划分为前端、中端、后端三个部分，包含了词法分析、语法分析、错误处理、中间代码生成、目标代码生成这几个总体的功能步骤，编译器采用一种近似LLVM的格式来作为中间代码，由前端SysY生成中间代码，再由中间代码生成MIPS后端代码（这样选择的原因是初始计划生成LLVM目标码，并按照此想法完成了代码生成一的部分，之后得知了由LLVM转化MIPS的思路，在不必完善LLVM目标码的基础上进行了对MIPS的转化）。

#### 接口设计

在`Complier.java`中主要包含了7个功能部分，其中包括编译主模块`unitSysY`，中间代码模块`unitLLVM`和目标代码模块`unitMIPS`，同时包含了错误处理符号表`symbolTableForError`和中间代码符号表`symbolTableForLLVM`，此外还由读入模块`reader`和写出模块`writer`。

模块处理的核心是递归下降程序，`unitSysY`通过`RCompUnit`来进行递归下降的语法分析，通过`RLLVM`函数来递归下降生成中间代码模块`unitLLVM`的逻辑结构，`unitLLVM`通过`RMIPS`函数递归下降生成目标代码`unitMIPS`的逻辑结构。

对于编译器的读入和输出，主要通过`reader`和`writer`配合一些功能函数来完成。输入代码通过`reader`读入文档解析token来完成，得到的token序列存放在tokens列表中。输出主要根据评测需要的5个功能模块，分别在其中的对应节点调用对应的输出即可。

- `writeTokens`输出词法分析结果
- `writeVn`输出语法分析结果
- `writeErrors`输出错误处理结果
- `writeLLVM`输出中间代码生成结果
- `writeMIPS`输出目标代码生成结果

#### 文件组织

在src目录下除了`Compiler.java`之外还有5个package，其包含的内容和结构为：

- `frontend`前端进行语法分析的主体结构，并且承担了递归下降构建中间代码逻辑结构的功能
- `midend`中端对应生成中间代码的部分，主体是LLVM的逻辑结构
- `backend`后端对应生成MIPS目标代码的部分，主体是MIPS逻辑结构
- `error`错误处理相关部分
- `utils`一些功能函数，以及reader和writer的实现

### 二、词法分析设计

词法分析部分，即构造Token类，并设置TokenType便于分析词法分析结果。

```java
enum TokenType{
	IDENFR("IDENFR",""),INTCON("INTCON",""),STRCON("STRCON",""),MAINTK("MAINTK","main"),
	CONSTTK("CONSTTK","const"),INTTK("INTTK","int"),BREAKTTK("BREAKTK","break"),
	CONTINUETK("CONTINUETK","continue"),IFTK("IFTK","if"),ELSETK("ELSETK","else"),
    NOT("NOT","!"),AND("AND","&&"),OR("OR","||"),WHILETK("WHILETK","while"),
	GETINTTK("GETINTTK","getint"),PRINTFTK("PRINTFTK","printf"),
    RETURNTK("RETURNTK","return"),PLUS("PLUS","+"),MINU("MINU","-"),
    VOIDTK("VOIDTK","void"),MULT("MULT","*"),DIV("DIV","/"),MOD("MOD","%"),
    LSS("LSS","<"),LEQ("LEQ","<="),GRE("GRE",">"),GEQ("GEQ",">="),EQL("EQL","=="),
    NEQ("NEQ","!="),ASSIGN("ASSIGN","="),SEMICN("SEMICN",";"),COMMA("COMMA",","),
    LPARENT("LPARENT","("),RPARENT("RPARENT",")"),LBRACK("LBRACK","["),
    RBRACK("RBRACK","]"),LBRACE("LBRACE","{"),RBRACE("RBRACE","}");
}
```

词法分析流程即根据词法分析状态图，依次对当前读入字符进行判断，并根据读入字符的情况，进行状态的转换或者进一步的token分析，每次`getToken`的过程解析出一个token，直到解析到读入文件结尾。

词法分析的输出即遵照评测要求进行输出，词法分析的结果保留在tokens列表中，等待下一个步骤的使用。

### 三、语法分析设计

#### 语法树建立

所有的语法单元都是继承了`Vn`类，该类的方法中提供了递归下降的程序入口，也提供了递归下降的模板函数。`Vn`就代表了一个语法单元的整体结构，`Vn`的类成员中包含了它所展开的儿子非终结符列表`vns`，依据此关系展开形成语法树结构。对于特殊的单元，例如`<AddExp>`等，尽管在文法中使用了递归的定义，在构建语法树的时候我并没有采取原始的文法结构，而是采用了扩展文法。为了保证输出的统一，在递归下降的输出函数中对语法结构进行了处理，补充了对应的输出。

```java
public int writeVnVt(Writer writer){
    for(int i = 0;i < this.vns.size();i++){
        Vn vn = this.vns.get(i);
        if(vn.IsVt()){
            Token token = vn.getToken();
            writer.addStr(token.getType().getName() + " " + token.getValue() + "\n");
        } else {
            vn.writeVnVt(writer);
            writer.addStr(this.name + "\n");
        }
    }
    return 0;
}
```

这个语法树建立的过程比较简单，整体来说这个语法树结构是后面错误处理、中间代码生成所依托的基础。

#### 递归下降过程

语法树的建立是通过递归下降实现的，递归下降过程和编译理论中的方法基本一致。这个递归下降的过程在后续错误处理和中间代码生成的过程中也有所体现，都是依据语法的树形结构进行进一步的分析。

### 四、错误处理设计

#### 语法分析处理

由于我们在处理时对一些情况进行了限制，因此不至于出现对于一些错误无法分辨错误类型的问题，基于这样的假设，将错误处理部分大大简化。在语法分析过程中的一些错误，比如缺失分号，缺失右括号这些，可以在语法分析递归下降的过程中可以直接进行处理，而对于语义方面的错误则等到进入新的递归下降的处理过程。

#### 符号表建立

对于语义错误，我采取的的方式是进行一次独立的递归下降过程，这样使得错误处理部分独立于代码生成部分，二者之间不存在交叉关系，但是会产生一部分冗余代码，这一部分后续还有很多改进空间。

语义错误处理的核心在于符号表部分，符号表的设计由`SymbolTable`类承担，`SymbolTable`中包含有`Symbol`列表，而一个`Symbol`有与它相关的部分信息，例如如下的两个枚举类就对应了一些信息。在符号定义时补全符号表，然后通过符号表来进行后续的验证。

```java
enum SymbolKind{
    VAR("var"),
    CONST("const"),
    FUNC("func"),
    PARA("para")
}
enum SymbolType{
    INT("int"),
    STR("str"),
    ARRAY("array")
}
```

### 五、代码生成设计

#### 值运算逻辑

对于值运算的过程大体可以分为普通值和布尔值两类。二者的共同之处在于，运算表达式的结果总是用一个唯一的值来承载，即可以对应LLVM中的一个寄存器。

首先以普通值为例，对于一个表达式而言，可以按照语法结构展开形成树形结构，处理的核心还是`<Exp>`这个语法单元，之后按照层级一直展开`<AddExp>`，`<MulExp>`，`<UnaryExp>`，每个语法单元都必然存在一个返回值，然后上层结构根据子结构的返回值进行计算。由于一个父结构可能存在不止一个子结构，因此在计算时应当遵循从左到右依次计算的原则，每检出一个运算符号就得到一个值（在实际中即对应分配的寄存器号）。

```java
public int RLLVM(SymbolTable symbolTable, Value value) {
    int lastindex = -1;
    int ansindex = 0;
    Vn op = null;
    if(!(value instanceof BasicBlock)){
        return 0;
    }
    value = (BasicBlock) value;
    for(Vn vn:vns){
        if(!vn.isVt){
            ansindex = vn.RLLVM(symbolTable,value);
            if(ansindex != -1){
                if(lastindex != -1){
                    int newindex = symbolTable.newReg();
                    addInstruction(value,symbolTable,newindex,op,lastindex,ansindex);
                    lastindex = newindex;
                } else {
                    lastindex = ansindex;
                }
            }
        } else {
            op = vn;
        }
    }
    return lastindex;
}
```

对于布尔值的计算则有所不同，因为布尔运算背后可能生成条件表达式判断，因此需要在表达式的处理上进行判断。大体上来说，如果需要用布尔值0或1来进行判断的情况，就使用值进行计算，计算的方式即按照优先级的顺序从左到右依次得到比较的结果，如`a < b < c`，即对于`a < b`的结果，即0或1再和c进行比较判断。而对于正常的条件比较，则可以直接通过分支语句进行比较。

```java
int indexrel = vnrel.vns.size() - 3;
Vn vnrel1 = vnrel.vns.get(indexrel + 1);
int retleft = ((RelExp) vnrel).RLLVM(symbolTable, blockeq, true, indexrel);
int retright = vnrel.vns.get(indexrel + 2).RLLVM(symbolTable, blockeq);
Operator opl = new Operator(VarType.INT, symbolTable.getRegByIndex(retleft));
Operator opr = new Operator(VarType.INT, symbolTable.getRegByIndex(retright));
if(vnrel1.getToken().getValue().equals(">")){
    blockeq.addInstruction(new BranchBle(opl, opr, blocktarget));
} else if(vnrel1.getToken().getValue().equals("<")) {
    blockeq.addInstruction(new BranchBge(opl, opr, blocktarget));
} else if(vnrel1.getToken().getValue().equals(">=")) {
    blockeq.addInstruction(new BranchBlt(opl, opr, blocktarget));
} else {
    blockeq.addInstruction(new BranchBgt(opl, opr, blocktarget));
}
```

#### 分支跳转逻辑

对于分支结果，由于需要进行短路求值，因此应当在代码生成基本块时对分支和跳转逻辑进行整理，使得最终代码逻辑结构更加清晰。

一般来说，可以将多个判断条件的短路求值过程转化连续的单个条件的判断：

```c
if (a || b) {
	...
} else {
    
}
```

可以转化为

```c
if (a) {
	...
} else if(b) {
    ...
} else {
    
}
```

对于while循环来说，可以通过引入`break`来将`while`循环的条件判断转变成if条件判断，即：

```c
while(a){
    ...
}
```

可以转化为

```c
while(1){
    if(a){
        ...
    } else {
        break;
    }
}
```

对于中间代码部分，逻辑判断和分支是可以直接得到不同的层次关系的，从基本块的划分角度来看，可以通过一次性的整体的划分来使得分支和条件判断过程更加简单清晰，在本次中间代码的生成过程中，就采用了以下的结构。我们目前对于逻辑划分就划分两层，首先的一层是或关系，这一层中只要有一组成立，即可跳转到成功条件的代码块，否则就进入下一个或关系（或者当处于最后一个或关系时进入失败条件的代码块）；或关系的子层是与关系，这一层中只要有一组不成立，即可跳转到下一个或关系进行判断（或者当处于最后一个或关系时进入失败条件的代码块）。

```
label_if_a:	
	if !a0 j label_if_b
	if !a1 j label_if_b
	... // 判断多条与关系
	j label_if_true
	
label_if_b:
	if !b0 j label_if_c
	if !b1 j label_if_c
	... // 判断多条与关系
	j label_if_true
	
... // 判断多条或关系a,b,...,n

label_if_n:
	if !n0 j label_if_false
	if !n1 j label_if_false
	... // 判断多条与关系
	j label_if_true

label_if_false:
	... // 错误分支的结果执行
	j label_if_end
	
label_if_true:
	... // 正确分支的结果执行
	
label_if_end:
	... // 结束条件判断
	
```

对于分支嵌套和循环的情况，大体上和本例类似，只需要通过相应的结构分布拆解，即可完成分支跳转部分的内容。

### 六、代码优化设计

很遗憾，限于时间原因暂无优化

### 结语

通过本学期的一个流程，感觉对编译的理解深刻了很多，自己写的一个编译器尽管还有很多不如意之处，但是总体说来是自己心血的结晶。感觉本次编译课还是留下了很多遗憾的，尤其是最终由于时间所限，原本计划的代码优化的一些想法没有去实现，希望后面可以有机会进行进一步的改进。
