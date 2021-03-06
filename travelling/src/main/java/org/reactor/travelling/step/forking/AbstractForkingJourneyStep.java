package org.reactor.travelling.step.forking;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import org.reactor.travelling.step.AbstractJourneyStep;
import org.reactor.travelling.step.AbstractJourneyStepDirection;
import org.reactor.travelling.step.JourneyStepVisitor;

public abstract class AbstractForkingJourneyStep<SUBJECT> extends AbstractJourneyStep<SUBJECT> {

    private Map<String, ForkingStepOutcome<SUBJECT>> outcomes = newHashMap();

    protected ForkingStepOutcome<SUBJECT> onStepInput(String stepInput) {
        ForkingStepOutcome<SUBJECT> outcome = new ForkingStepOutcome<>();
        outcomes.put(stepInput, outcome);
        return outcome;
    }

    public final AbstractJourneyStepDirection<SUBJECT> doStep(String stepInput, SUBJECT journeySubject) {
        if (outcomes.containsKey(stepInput)) {
            doBeforeForking(stepInput, journeySubject);
            return fork(outcomes.get(stepInput));
        }
        return repeat();
    }

    protected abstract void doBeforeForking(String stepInput, SUBJECT journeySubject);

    protected final AbstractJourneyStepDirection<SUBJECT> fork(final ForkingStepOutcome<SUBJECT> forkingStepOutcome) {
        return new AbstractJourneyStepDirection<SUBJECT>() {

            @Override
            public void followDirection(JourneyStepVisitor<SUBJECT> journeyStepVisitor) {
                if (forkingStepOutcome.isEmpty()) {
                    journeyStepVisitor.moveForward();
                    return;
                }
                journeyStepVisitor.fork(forkingStepOutcome);
            }
        };
    }

}
