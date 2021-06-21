/* B-ZKCK-62.ts

	Purpose:
		
	Description:
		
	History:
		Mon Jun 21 14:33:05 CST 2021, Created by rudyhuang

Copyright (C) 2021 Potix Corporation. All Rights Reserved.
*/
import { Selector } from 'testcafe';

fixture('ZKCK-62')
	.skip // drag bug: https://github.com/DevExpress/testcafe/issues/5148
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-62.zul');

test('test', async t => {
	const ckeditor = Selector('.z-ckeditor'),
		resizer = Selector('.cke_1 .cke_resizer'),
		oldHeight = await ckeditor.offsetHeight;

	await t
		.drag(resizer, 0, -100)
		.expect(ckeditor.offsetHeight).lt(oldHeight, 'ckeditor should be smaller');
});