package io.bdc.painttd.content;

import io.bdc.painttd.content.trajector.*;
import io.bdc.painttd.content.trajector.processor.*;

public class Paths{
    public static Processor
    seq = new SeqProcessor(64),
    par = new ParallelProcessor(64),
    scl = new ScaleProcessor(),
    line = new LineProcessor();
}

