#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test010 {
		void __A::methodSetA(A __this, String x) {
			__this->a = x;
		};

		void __A::methodPrintOther(A __this, A other) {
			
			try {
				std::stringstream ss;
				ss << other;
				std::string tmp = ss.str();
				int count = 0;
				for(int i = 0; i < tmp.length(); i++){
					if(tmp[i] != '0'){ count += 1; }
				}
				if(count == 2 || count == 1){ throw java::lang::NullPointerException(); }
				Class k = other->__vptr->getClass(other);
				std::string paramClass = k->__vptr->getName(k)->data;
				Class thisK = __this->__vptr->getClass(__this);
				std::string thisClass = thisK->__vptr->getName(thisK)->data;
				//if(paramClass != thisClass){ throw java::lang::ClassCastException();}
				cout << other->__vptr->toString(other)->data << endl;
			}
			catch(const NullPointerException &ex){
				cout << "java.lang.NullPointerException" << endl;
				cout << "	at inputs.test010.A.methodPrintOther" << endl;
			}
			catch(const ClassCastException &ex){
				cout << "java.lang.ClassCastException" << endl;
				cout << "	at inputs.test010.A" << endl;
			}

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

		Class __B1::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test010.B1"), (Class) __rt::null());
			return k;
		};

		__B1_VT __B1::__vtable;

		__B2::__B2() : __vptr(&__vtable) {};

		Class __B2::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test010.B2"), (Class) __rt::null());
			return k;
		};

		__B2_VT __B2::__vtable;

		__C::__C() : __vptr(&__vtable) {};

		Class __C::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test010.C"), (Class) __rt::null());
			return k;
		};

		__C_VT __C::__vtable;

	}
}
