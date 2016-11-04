
//------------------

#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs{
	namespace test004{

	__A::__A(String fld) : __vptr(&__vtable)  {

		fld(fld)
		
	}

	__A_VT __A::__vtable;

	String __A::getFld(A __this) {
		return  __this->fld;
	}

	Class __A::__class() {
		static Class k =
			new __Class(__rt::literal("class inputs.javalang.A"), (Class) __rt::null());
 		return k;
	}


	}
}

//------------------

