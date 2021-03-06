/*
 * AngularBeans, CDI-AngularJS bridge 
 *
 * Copyright (c) 2014, Bessem Hmidi. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 */

package angularBeans.ngservices;

/**
 * 
 * responseHandler angularJs Service to handle any server response (Half-duplex
 * or realtime protocol)
 * 
 * @author Bessem Hmidi
 *
 */

@NGExtension
public class ResponseHandlerService implements NGService {

	@Override
	public String render() {

		String result = "";

		result += "app.service('responseHandler',['logger','$rootScope','$filter',function(logger,$rootScope,$filter){\n";

		result += ("\nthis.handleResponse=function(msg,caller,isRPC){");

	//	result +="console.log(angular.toJson(msg));";
	//	 result +="console.log('--'+JSON.stringify(msg));";

		result += "var mainReturn={};";

		result += ("\nfor (var key in msg) {");
		
		
		
		result += ("\nif(key==='rootScope'){");

		result += ("\nfor(var model in msg[key]){");

		result += ("\n$rootScope[model]=msg['rootScope'][model];");

		result += ("\n}");
		result += ("}");

		result += ("\nif((key==='zadd')||(key==='rm')||(key==='rm-k')){");

		result += "var equalsKey='--';";

		result += ("\nfor(var modelkey in msg[key]){");
		//

		result += "if (!(angular.isDefined(caller[modelkey]))){"
				+ "caller[modelkey]=[]; }";

		result += "var tab=msg[key][modelkey];";

		result += "for (var value in tab){";

		result += "if (typeof tab[value] == 'string' || tab[value] instanceof String){";

		result += "if(tab[value].indexOf('equalsKey:') > -1){equalsKey=tab[value].replace('equalsKey:',''); ;}}}";

		//
		result += "for (var value in tab){";

		result += "if((key==='rm')||(key==='rm-k')){";
		result += "if(equalsKey=='NAN'){";
		result += "if(tab[value]==='equalsKey:NAN'){continue;}";

		result += "var index=angularBeans.isIn(caller[modelkey],tab[value]);";
		result += "if(index>-1){caller[modelkey].splice(index, 1);continue;}";
		result += "}"

		+ "else{";
	
		result += "var criteria={};";
		result += "criteria[equalsKey]='!'+tab[value];";
		
		// result +="console.log(JSON.stringify(tab[value]));";
		 
		result += "caller[modelkey] = $filter('filter')(caller[modelkey], criteria);";
		result += "}};";

		result += "if(key==='zadd'){ ";
		result += "\n var found=false; ";

		result += "if(angularBeans.isIn(caller[modelkey],tab[value])>-1){ found=true;}";

		result += "if(!(found)){  ";
		result += "caller[modelkey].push(tab[value]);";
		result += "}};"

		+ "}";

		result += ("}");

		result += ("\n  }");

		// --------------------------------------------------------------------

		result += ("if(!(key in ['rootScope','zadd','mainReturn','rm','rm-k'])){");

		result += ("\ncaller[key]=msg[key];");

		result += ("\n  }");
 
		result += "if ((key==='mainReturn')&&(msg[key])){"
				+ "if(msg[key].hasOwnProperty('boundTo')){"
				+ "mainReturn=msg[msg[key].boundTo];"

				+ "}else{"

				+ "mainReturn=msg[key];}}";

		result += ("\n  }");
			
		result+="if(!$rootScope.$$phase) {$rootScope. $digest ;$rootScope.$apply();}";
	    result += "if(!isRPC){$rootScope. $digest ;$rootScope.$apply();}";
        result+="if(msg.log){logger.log(msg.log);}";
		result += ("return mainReturn;");

		result += ("};");

		result += ("}]);\n");

		return result;
	}

}
