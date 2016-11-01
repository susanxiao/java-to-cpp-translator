

//------------------

#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs{
	namespace test006{

	__A::__A() : __vptr(&__vtable)  {
		fld = new __String("A");

	}

	__A_VT __A::__vtable;

	void __A::setFld(A __this,String f) {
		__this->fld = f;
	}

	void __A::almostSetFld(String f) {
		String fld;
		fld = f;
	}

	String __A::getFld(A __this) {
		return  __this->fld;
	}

	int32_t __A::hashCode(A __this){
		return 5;
	}

	Class __A::__class() {
		static Class k =
			new __Class(__rt::literal("class inputs.javalang.A"), (Class) __rt::null());
 		return k;
	}


	}
}

//------------------


