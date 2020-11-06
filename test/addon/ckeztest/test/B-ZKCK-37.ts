/* B-ZKCK-37.ts

	Purpose:
		
	Description:
		
	History:
		Fri Nov 09 15:15:48 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/

import { Selector } from 'testcafe';

fixture('ZKCK-37')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-37.zul');

test('test', async (t) => {
	await t.resizeWindow(800, 600)
		.click('.cke_button__maximize_icon');
	let content = Selector('#cke_1_contents'),
		contentHeight = await content.offsetHeight;
	await t.resizeWindow(700, 600)
		.expect(content.offsetHeight).eql(contentHeight);
});

