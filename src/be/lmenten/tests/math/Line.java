package be.lmenten.tests.math;

public class Line
{
	double length;
	double angle;

	Line( double length, double angle )
	{
		this.length = length;
		this.angle = angle;
	}

	public double getLength()
	{
		return length;
	}

	public void setLength( double length )
	{
		this.length = length;
	}

	public double getAngle()
	{
		return angle;
	}
}
