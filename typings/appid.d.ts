declare module AppID {

   
	function initialize(success: Function, failure: Function, tenantId?: string,region?: string): void;
	
	function login(success: Function, failure: Function, username?: string,password?: string): void;

	function logout(success: Function, failure: Function): void;

}