#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test010 {
		void __A::setA(A __this, String x) {
			__this->a = x;
		};

		void __A::printOther(A __this, A other) {
			cout << other->__vptr->toString(other)->data << endl;
		};

		String __A::toString(A __this) {
			return __this->a;
		};

		__A::__A() : __vptr(&__vtable) {};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test010.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

		__B1::__B1() : __vptr(&__vtable) {};

		String __B1::toString(B1 __this) {
			Class k = __this->__vptr->getClass(__this);
			std::ostringstream sout;
			sout << k->__vptr->getName(k)->data
				<< '@' << std::hex << (uintptr_t) __this;
			return new __String(sout.str());
		};
		Class __B1::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test010.B1"), (Class) __rt::null());
			return k;
		};

		__B1_VT __B1::__vtable;

		__B2::__B2() : __vptr(&__vtable) {};

		String __B2::toString(B2 __this) {
			Class k = __this->__vptr->getClass(__this);
			std::ostringstream sout;
			sout << k->__vptr->getName(k)->data
				<< '@' << std::hex << (uintptr_t) __this;
			return new __String(sout.str());
		};
		Class __B2::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test010.B2"), (Class) __rt::null());
			return k;
		};

		__B2_VT __B2::__vtable;

		__C::__C() : __vptr(&__vtable) {};

		String __C::toString(C __this) {
			Class k = __this->__vptr->getClass(__this);
			std::ostringstream sout;
			sout << k->__vptr->getName(k)->data
				<< '@' << std::hex << (uintptr_t) __this;
			return new __String(sout.str());
		};
		Class __C::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test010.C"), (Class) __rt::null());
			return k;
		};

		__C_VT __C::__vtable;

	}
}
