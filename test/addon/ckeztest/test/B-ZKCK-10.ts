/* B-ZKCK-10.ts

	Purpose:
		
	Description:
		
	History:
		Mon Nov 09 15:54:28 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/

import assertNoConsoleError from './check-console-error';

fixture('ZKCK-10')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-10.zul')

test('test', async t => {
	await t.click('.z-button')
		.click('.z-window-header')
		.click('.z-button');
	await assertNoConsoleError();
});