package edu.sjsu.fwjs;

import java.util.ArrayList;
import java.util.List;

/**
 * FWJS expressions.
 */
public interface Expression {
    /**
     * Evaluate the expression in the context of the specified environment.
     */
    public Value evaluate(Environment env);
}

// NOTE: Using package access so that all implementations of Expression
// can be included in the same file.

/**
 * FWJS constants.
 */
class ValueExpr implements Expression {
    private Value val;
    public ValueExpr(Value v) {
        this.val = v;
    }
    public Value evaluate(Environment env) {
        return this.val;
    }
}

/**
 * Expressions that are a FWJS variable.
 */
class VarExpr implements Expression {
    private String varName;
    public VarExpr(String varName) {
        this.varName = varName;
    }
    public Value evaluate(Environment env) {
        return env.resolveVar(varName);
    }
}

/**
 * A print expression.
 */
class PrintExpr implements Expression {
    private Expression exp;
    public PrintExpr(Expression exp) {
        this.exp = exp;
    }
    public Value evaluate(Environment env) {
        Value v = exp.evaluate(env);
        System.out.println(v.toString());
        return v;
    }
}
/**
 * Binary operators (+, -, *, etc).
 * Currently only numbers are supported.
 */
class BinOpExpr implements Expression {
    private Op op;
    private Expression e1;
    private Expression e2;
    public BinOpExpr(Op op, Expression e1, Expression e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    @SuppressWarnings("incomplete-switch")
    public Value evaluate(Environment env) {
        int a = ((IntVal) e1.evaluate(env)).toInt();
    	int b = ((IntVal) e2.evaluate(env)).toInt();
    	
    	switch(op) {
    	case ADD:
    		return new IntVal(a + b);
    	case SUBTRACT:
    		return new IntVal(a - b);
    	case MULTIPLY:
    		return new IntVal(a * b);
    	case DIVIDE:
            return new IntVal(a / b);
        case EQ:
    		return new BoolVal(a == b);
    	case MOD:
    		return new IntVal(a % b);
    	case GT:
    		return new BoolVal(a > b);
    	case GE:
    		return new BoolVal(a >= b);
    	case LT:
    		return new BoolVal(a < b);
    	case LE:
    		return new BoolVal(a <= b);
    	default:
    		return null;
    	}
    }
}

/**
 * If-then-else expressions.
 * Unlike JS, if expressions return a value.
 */
class IfExpr implements Expression {
    private Expression cond;
    private Expression thn;
    private Expression els;
    public IfExpr(Expression cond, Expression thn, Expression els) {
        this.cond = cond;
        this.thn = thn;
        this.els = els;
    }
    public Value evaluate(Environment env) {
        BoolVal b1 = (BoolVal) cond.evaluate(env);
        if(b1.toBoolean()){
            return thn.evaluate(env);
        } else{
            return els.evaluate(env);
        }
    }
}

/**
 * While statements (treated as expressions in FWJS, unlike JS).
 */
class WhileExpr implements Expression {
    private Expression cond;
    private Expression body;
    public WhileExpr(Expression cond, Expression body) {
        this.cond = cond;
        this.body = body;
    }
    public Value evaluate(Environment env) {
        BoolVal flag = (BoolVal) cond.evaluate(env);        
        if(flag.toBoolean())
        {
            body.evaluate(env);
            return this.evaluate(env);
        } else
        {
            return null;
        }
        
//      BoolVal flag = (BoolVal) cond.evaluate(env);
//      Value result ;
//      
//        while(flag.toBoolean()) {
//          result = body.evaluate(env);
//          flag = (BoolVal) cond.evaluate(env);
//        } 

        
        
        
//        while(cond.evaluate(env).toString() == "true") {
//          result = body.evaluate(env);
        

        //return null;
        return null;
    }
}

/**
 * Sequence expressions (i.e. 2 back-to-back expressions).
 */
class SeqExpr implements Expression {
    private Expression e1;
    private Expression e2;
    public SeqExpr(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }
    public Value evaluate(Environment env) {
    	e1.evaluate(env);
        return e2.evaluate(env);
    }
}

/**
 * Declaring a variable in the local scope.
 */
class VarDeclExpr implements Expression {
    private String varName;
    private Expression exp;
    public VarDeclExpr(String varName, Expression exp) {
        this.varName = varName;
        this.exp = exp;
    }
    public Value evaluate(Environment env) {
        Value v1 = this.exp.evaluate(env);
        env.createVar(varName, v1);

        return v1;
    }
}

/**
 * Updating an existing variable.
 * If the variable is not set already, it is added
 * to the global scope.
 */
class AssignExpr implements Expression {
    private String varName;
    private Expression e;  // Should be ValueExpr class
    public AssignExpr(String varName, Expression e) {
        this.varName = varName;
        this.e = e;
    }
    public Value evaluate(Environment env) {
        env.updateVar(varName, e.evaluate(env));
        return e.evaluate(env);  // Same as ValueExpr.evaluate(env);
    }
}

/**
 * A function declaration, which evaluates to a closure.
 */
class FunctionDeclExpr implements Expression {
    private List<String> params;
    private Expression body;
    public FunctionDeclExpr(List<String> params, Expression body) {
        this.params = params;
        this.body = body;
    }
    public Value evaluate(Environment env) {
        return new ClosureVal(params, body, env);
    }
}

/**
 * Function application.
 */
class FunctionAppExpr implements Expression {
    private Expression f;  // Evaluates to the closure object
    private List<Expression> args;
    public FunctionAppExpr(Expression f, List<Expression> args) {
        this.f = f;
        this.args = args;
    }
    public Value evaluate(Environment env) {
    	List<Value> valArgs= new ArrayList<Value>();
    	
    	ClosureVal closure = (ClosureVal) f.evaluate(env);
    	
        for (Expression exp: args){
        	valArgs.add(exp.evaluate(new Environment(env)));
        }
        
        return closure.apply(valArgs);
    }
}

