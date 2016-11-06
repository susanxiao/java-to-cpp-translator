#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test013 {
		struct __A;
		struct __A_VT;


		typedef __A* A;

		struct __A {
			__A_VT* __vptr;
			String a;

			__A();

			static Class __class();

			static __A_VT __vtable;

			static void setA(A, String);
			static void printOther(A, A);
		};

		struct __A_VT {
			Class __isa;

			bool (*equals)(A, Object);
			Class (*getClass)(A);
			int32_t (*hashCode)(A);
			void (*printOther)(A, A);
			void (*setA)(A, String);
			String (*toString)(A);

			__A_VT()
			: __isa(__A::__class()),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			printOther(&__A::printOther),
			setA(&__A::setA),
			toString((String(*)(A))&__Object::toString)
			{}
		};

	}
}