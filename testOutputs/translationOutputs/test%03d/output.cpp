#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test006 {
		__A::__A() : __vptr(&__vtable) ,
				fld(new __String("A")) {
		};

		void __A::methodSetFld(A __this, String f) {
			__rt::checkNotNull(f);
			__this->fld = f;
		};

		void __A::methodAlmostSetFld(A __this, String f) {
			__rt::checkNotNull(f);
			String fld;
			fld = f;
		};

		String __A::methodGetFld(A __this) {
			return __this->fld;
		};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test006.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}

