
//------------------

#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs{
	namespace test003{

	__A::__A(String f) : __vptr(&__vtable),
		fld(f)  {
		
	}

	__A_VT __A::__vtable;

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


