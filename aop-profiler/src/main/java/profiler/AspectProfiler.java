package profiler;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;

import java.util.function.Consumer;

@Aspect
public class AspectProfiler {

    private boolean filterPackage(Signature signature) {
        return signature.getDeclaringType().getPackageName().startsWith(CallStackProfiler.getPackageName());
    }

    private void registerIfNeeded(JoinPoint.StaticPart thisJoinPoint, Consumer<Signature> registrar) {
        Signature signature = thisJoinPoint.getSignature();
        if (filterPackage(signature)) {
            registrar.accept(signature);
        }
    }

    @Pointcut("call(!static * *.*(..)) && !within(profiler.*)")
    public void profile() {}

    @Before("profile()")
    public void beforeMethod(JoinPoint.StaticPart thisJoinPoint) {
        registerIfNeeded(thisJoinPoint, CallStackProfiler.INSTANCE::methodCall);
    }

    @After("profile()")
    public void afterMethod(JoinPoint.StaticPart thisJoinPoint) {
        registerIfNeeded(thisJoinPoint, CallStackProfiler.INSTANCE::methodExit);
    }

    @AfterThrowing(value = "profile()", throwing = "t")
    public void afterException(JoinPoint.StaticPart thisJoinPoint, Throwable t) {
        registerIfNeeded(
            thisJoinPoint,
            signature -> CallStackProfiler.INSTANCE.methodFailed(signature, t)
        );
    }
}
