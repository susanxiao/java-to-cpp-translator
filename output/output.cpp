#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test017 {
		__A::__A(int x) : __vptr(&__vtable) {
		};

		A __A::self(A __this) {
			return __this->self;
		};

		String __A::toString(A __this) {
			Class k = __this->__vptr->getClass(__this);
			std::ostringstream sout;
			sout << k->__vptr->getName(k)->data
				<< '@' << std::hex << (uintptr_t) __this;
			return new __String(sout.str());
		};
		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test017.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}
