/* B-ZKCK-38.ts

	Purpose:
		
	Description:
		
	History:
		Tue Nov 10 10:52:01 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/

import { Selector } from "testcafe";

fixture('ZKCK-38')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-38.zul');

let getTopElement = Selector(() => document.elementFromPoint(1, 1));

test('test', async (t) => {
	await t.resizeWindow(800, 600)
		.click('.cke_button__maximize_icon');

	let topElement = await getTopElement();

	await t.resizeWindow(700, 600)
		.expect(getTopElement().classNames)
		.eql(topElement.classNames);
});
