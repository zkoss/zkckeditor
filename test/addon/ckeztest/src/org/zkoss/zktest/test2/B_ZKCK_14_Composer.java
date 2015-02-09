/* B_ZKCK_14_Composer.java

	Purpose:
		
	Description:
		
	History:
		Fri, Feb 06, 2015  6:00:38 PM, Created by Han

Copyright (C)  Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.zktest.test2;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;

/**
 * 
 * @author Han
 */
public class B_ZKCK_14_Composer extends GenericForwardComposer<Component> {

	private static final long serialVersionUID = 595477172241551647L;

	private String instructionText;

    private Instruction instruction;

    public Instruction getInstruction() {
        return this.instruction;
    }

    public void setInstruction(final Instruction instruction) {
        this.instruction = instruction;
    }

    public String getInstructionText() {
        return this.instructionText;
    }

    public void setInstructionText(final String instructionText) {
        this.instructionText = instructionText;
    }

    public class Instruction {
        private String text;
        private String title;

        public String getText() {
            return this.text;
        }

        public void setText(final String text) {
            this.text = text;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(final String title) {
            this.title = title;
        }
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        this.instructionText = null;
        this.instruction = new Instruction();
    }
}
