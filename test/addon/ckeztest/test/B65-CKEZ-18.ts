import { Selector } from 'testcafe';
import checkConsoleError from './check-console-error';

fixture('B65-CKEZ-18')
	.page('http://localhost:8080/ckeztest/test2/B65-CKEZ-18.zul');

test('test', async t => {
	await t
		.click('.z-window-close');
	await checkConsoleError();

	await t
		.click('.z-panel-close');
	await checkConsoleError();
});