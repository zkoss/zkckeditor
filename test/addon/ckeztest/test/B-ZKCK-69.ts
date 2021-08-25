/* B-ZKCK-69.ts

	Purpose:
		
	Description:
		
	History:
		Mon Aug 23 18:08:15 CST 2021, Created by katherine

Copyright (C) 2021 Potix Corporation. All Rights Reserved.
*/
import { Selector } from 'testcafe';

fixture('ZKCK-69')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-69.zul');

test('test', async t => {
	await t
		.click('.btn')
		.expect(Selector('.cke_contents').offsetHeight).eql(200);
});