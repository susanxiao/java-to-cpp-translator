#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test034 {
		int32_t __A::methodMByte(A __this, uint8_t b) {
			__rt::checkNotNull(__this);
			cout << "A.m(byte)" << endl;
			return b;
		};

		int32_t __A::methodMInt(A __this, int32_t i) {
			__rt::checkNotNull(__this);
			cout << "A.m(int)" << endl;
			return i;
		};

		void __A::methodMDouble(A __this, double d) {
			__rt::checkNotNull(__this);
			cout << "A.m(double)" << endl;
		};

		__A::__A() : __vptr(&__vtable)
		{};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test034.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}

