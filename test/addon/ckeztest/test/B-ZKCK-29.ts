/* B-ZKCK-29.ts

	Purpose:
		
	Description:
		
	History:
		Fri Nov 06 15:15:48 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/

import { Selector } from 'testcafe';

fixture('ZKCK-29')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-29.zul');

test('test', async (t) => {
	let ckeditors = Selector('.z-ckeditor'),
		ck0 = ckeditors.nth(0),
		ck1 = ckeditors.nth(1),
		ck2 = ckeditors.nth(2);

	await ck0.find('.cke').exists;
	await ck1.find('.cke').exists;
	await ck2.find('.cke').exists;
	let heightCk0 = await ck0.offsetHeight,
		heightCk1 = await ck1.offsetHeight,
		heightCk2 = await ck2.offsetHeight;

	await t.expect(heightCk0).eql(heightCk1)
		.expect(heightCk1).eql(heightCk2);
});

