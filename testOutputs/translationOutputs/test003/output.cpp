#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test003 {
		__A::__A(String f) : __vptr(&__vtable) ,
				fld(f) {
		};

		String __A::methodGetFld(A __this) {
			__rt::checkNotNull(__this);
			return __this->fld;
		};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test003.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}

