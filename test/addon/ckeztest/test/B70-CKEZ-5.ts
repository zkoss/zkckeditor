/* B70-CKEZ-5.ts

	Purpose:
		
	Description:
		
	History:
		Mon Nov 09 16:12:48 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/

import { Selector } from 'testcafe';

fixture('B70-CKEZ-5')
	.page('http://localhost:8080/ckeztest/test2/B70-CKEZ-5.zul');

test('test', async t => {
	await t.resizeWindow(800, 600);
	let editableFrame = Selector('.cke_wysiwyg_frame'),
		editorWidth = await Selector('.cke_1').clientWidth;

	await t.click(editableFrame)
		.pressKey('1 enter 1 enter 1 enter 1 enter 1 enter 1 enter 1 enter 1 enter 1 enter 1 enter')
		.resizeWindow(400, 600)
		.resizeWindow(800, 600)
		.expect(editableFrame.offsetWidth).eql(editorWidth);
});