/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.utils.math.evaluator.grammar.node;

import be.lmenten.utils.math.evaluator.grammar.analysis.*;

@SuppressWarnings("nls")
public final class TOpGThan extends Token
{
    public TOpGThan()
    {
        super.setText(">");
    }

    public TOpGThan(int line, int pos)
    {
        super.setText(">");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TOpGThan(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTOpGThan(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TOpGThan text.");
    }
}
