/* B-ZKCK-67.ts

	Purpose:
		
	Description:
		
	History:
		Fri Aug 06 17:18:22 CST 2021, Created by katherine

Copyright (C) 2021 Potix Corporation. All Rights Reserved.
*/
import { Selector } from 'testcafe';

fixture('ZKCK-67')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-67.zul');

test('test', async t => {
	const ckeditor = Selector('.z-ckeditor'),
		resizer = Selector('.cke_resizer');

	await t
		.expect(ckeditor.nth(0).offsetHeight).eql(200)
		.expect(ckeditor.nth(1).offsetHeight).eql(201)
		.expect(ckeditor.nth(2).offsetHeight).eql(400)
		.expect(resizer.nth(0).visible).ok()
		.expect(resizer.nth(1).visible).notOk();

	await t
		.click('.btn')
		.wait(100)
		.expect(Selector('.cke_resizer').nth(1).visible).ok();
});