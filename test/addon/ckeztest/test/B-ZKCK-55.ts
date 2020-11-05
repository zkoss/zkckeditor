/* B-ZKCK-55.ts

	Purpose:
		
	Description:
		
	History:
		Thu Nov 05 17:46:28 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
import { Selector, ClientFunction } from 'testcafe';
import checkConsoleError from './check-console-error';

fixture('ZKCK-55');

const getCkeditorInstanceCount = ClientFunction(() =>
	Object.keys(window['CKEDITOR']['instances']).length
);

test.page('http://localhost:8080/ckeztest/test2/B-ZKCK-55.zul')
('test', async t => {
	let buttons = Selector('.z-button');

	for (let i = 0; i < 10; i++) {
		await t
			.click(buttons.nth(0))
			.wait(300);
	}
	await checkConsoleError();
	await t.expect(getCkeditorInstanceCount()).eql(0);
});


test.page('http://localhost:8080/ckeztest/test2/B-ZKCK-55-2.zul')
('test with a Tabbox', async t => {
	let tab2 = Selector('.z-tab').nth(1);

	await t.click(tab2);
	await checkConsoleError();

	for (let i = 0; i < 10; i++) {
		await t
			.click(tab2)
			.wait(100);
	}
	await checkConsoleError();
	await t.expect(getCkeditorInstanceCount()).eql(1);
});