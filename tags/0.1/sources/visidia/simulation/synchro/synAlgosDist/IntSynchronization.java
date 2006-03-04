package visidia.simulation.synchro.synAlgosDist;

import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synObj.*;

import visidia.simulation.rulesDist.*;
import visidia.simulation.AlgorithmDist;
import visidia.rule.Star;
import visidia.simulation.Simulator;
import java.util.Vector;

public interface IntSynchronization{
    
    public void trySynchronize();
    public void set(AlgorithmDist a);

}
