/* B-ZKCK-31.ts

	Purpose:
		
	Description:
		
	History:
		Tue Nov 10 14:56:46 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/

import { Selector } from 'testcafe';

fixture('ZKCK-31')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-31.zul');

test('test', async t => {
	await t
		.resizeWindow(800, 600)
		.click('.z-button')
		.click('.cke_button__maximize')
		.resizeWindow(700, 600)
		.expect(Selector('.cke_maximized').getBoundingClientRectProperty('top'))
		.eql(0);
});

