/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.utils.math.evaluator.grammar.node;

import be.lmenten.utils.math.evaluator.grammar.analysis.*;

@SuppressWarnings("nls")
public final class AFunc3Exp extends PExp
{
    private TIdentifier _identifier_;
    private PExp _left_;
    private PExp _center_;
    private PExp _right_;

    public AFunc3Exp()
    {
        // Constructor
    }

    public AFunc3Exp(
        @SuppressWarnings("hiding") TIdentifier _identifier_,
        @SuppressWarnings("hiding") PExp _left_,
        @SuppressWarnings("hiding") PExp _center_,
        @SuppressWarnings("hiding") PExp _right_)
    {
        // Constructor
        setIdentifier(_identifier_);

        setLeft(_left_);

        setCenter(_center_);

        setRight(_right_);

    }

    @Override
    public Object clone()
    {
        return new AFunc3Exp(
            cloneNode(this._identifier_),
            cloneNode(this._left_),
            cloneNode(this._center_),
            cloneNode(this._right_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFunc3Exp(this);
    }

    public TIdentifier getIdentifier()
    {
        return this._identifier_;
    }

    public void setIdentifier(TIdentifier node)
    {
        if(this._identifier_ != null)
        {
            this._identifier_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._identifier_ = node;
    }

    public PExp getLeft()
    {
        return this._left_;
    }

    public void setLeft(PExp node)
    {
        if(this._left_ != null)
        {
            this._left_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._left_ = node;
    }

    public PExp getCenter()
    {
        return this._center_;
    }

    public void setCenter(PExp node)
    {
        if(this._center_ != null)
        {
            this._center_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._center_ = node;
    }

    public PExp getRight()
    {
        return this._right_;
    }

    public void setRight(PExp node)
    {
        if(this._right_ != null)
        {
            this._right_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._right_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._identifier_)
            + toString(this._left_)
            + toString(this._center_)
            + toString(this._right_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._identifier_ == child)
        {
            this._identifier_ = null;
            return;
        }

        if(this._left_ == child)
        {
            this._left_ = null;
            return;
        }

        if(this._center_ == child)
        {
            this._center_ = null;
            return;
        }

        if(this._right_ == child)
        {
            this._right_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

        if(this._left_ == oldChild)
        {
            setLeft((PExp) newChild);
            return;
        }

        if(this._center_ == oldChild)
        {
            setCenter((PExp) newChild);
            return;
        }

        if(this._right_ == oldChild)
        {
            setRight((PExp) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}