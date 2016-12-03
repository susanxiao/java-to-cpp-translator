#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test004 {
		__A::__A(String fld) : __vptr(&__vtable) ,
				fld(fld) {
		};

		String __A::methodGetFld(A __this) {
			return __this->fld;
		};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test004.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}

