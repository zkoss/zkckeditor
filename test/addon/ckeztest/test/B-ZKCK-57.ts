/* B-ZKCK-57.ts

	Purpose:
		
	Description:
		
	History:
		Wed Oct 14 17:08:00 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
import { Selector } from 'testcafe';

fixture('ZKCK-57')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-57.zul');

test('test', async t => {
	const div = Selector('.z-div'),
		ckeditor = Selector('.z-ckeditor');

	await t.expect(ckeditor.offsetHeight).gte(await div.clientHeight, 'ckeditor is smaller than div');
});