#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test006 {
		__A::__A() : __vptr(&__vtable),
				fld(new __String("A")) {
		};

		void __A::setFld(A __this, String f) {
			__this->_fld = f;
		};

		void __A::almostSetFld(A __this, String f) {
			String fld;
			_fld = f;
		};

		String __A::getFld(A __this) {
			return __this->_fld;
		};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test006.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}
