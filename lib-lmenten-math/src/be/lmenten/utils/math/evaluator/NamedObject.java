/* ************************************************************************* *
 * Dynamic Expression for mXparser/java * Copyright (c) 2022+ Laurent MENTEN *
 * ------------------------------------------------------------------------- *
 * BSD 2-Clause "Simplified" Licence.                                        *
 *                                                                           *
 * You may use this software under the condition of Simplified BSD License.  *
 * Redistribution and use in source and binary forms, with or without        *
 * modification, are permitted provided that the following conditions are    *
 * met:                                                                      *
 *                                                                           *
 * 1. Redistributions of source code must retain the above copyright notice, *
 *    this list of conditions and the following disclaimer.                  *
 * 2. Redistributions in binary form must reproduce the above copyright      *
 *    notice, this list of conditions and the following disclaimer in the    *
 *    documentation and/or other materials provided with the distribution.   *
 *                                                                           *
 * THIS SOFTWARE IS PROVIDED BY LAURENT MENTEN "AS IS" AND ANY EXPRESS OR    *
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES *
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.   *
 * IN NO EVENT SHALL LAURENT MENTEN OR CONTRIBUTORS BE LIABLE FOR ANY        *
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL        *
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS   *
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)     *
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,       *
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  *
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE           *
 * POSSIBILITY OF SUCH DAMAGE.                                               *
 * ************************************************************************* */

package be.lmenten.utils.math.evaluator;

public class NamedObject
	implements Value
{
	private final String name;
	private final Object object;

	/**
	 *
	 * @param name
	 * @param object
	 */
	public NamedObject( String name, Object object )
	{
		this.name = name;
		this.object = object;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 *
	 * @return
	 */
	public Object getObject()
	{
		return object;
	}
}
