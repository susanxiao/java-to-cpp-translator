#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test009 {
		__A::__A() : __vptr(&__vtable) {
			self = this;
		};


		String __A::toString(A __this){
			Class k = __this->__vptr->getClass(__this);
			std::ostringstream sout;
			sout << k->__vptr->getName(k)->data
				 << '@' << std::hex << (uintptr_t) __this;
			return new __String(sout.str());
		}

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test009.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

		__B::__B() : __vptr(&__vtable) {};


		String __B::toString(B __this){
			Class k = __this->__vptr->getClass(__this);
			std::ostringstream sout;
			sout << k->__vptr->getName(k)->data
				 << '@' << std::hex << (uintptr_t) __this;
			return new __String(sout.str());
		}

		Class __B::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test009.B"), (Class) __rt::null());
			return k;
		};

		__B_VT __B::__vtable;

	}
}
