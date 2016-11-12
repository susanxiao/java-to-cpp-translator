#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test010 {
		struct __A;
		struct __A_VT;

		struct __B1;
		struct __B1_VT;

		struct __B2;
		struct __B2_VT;

		struct __C;
		struct __C_VT;


		typedef __A* A;
		typedef __B1* B1;
		typedef __B2* B2;
		typedef __C* C;

		struct __A {
			__A_VT* __vptr;
			String a;

			__A();

			static Class __class();

			static __A_VT __vtable;

			static void setA(A, String);
			static void printOther(A, A);
			static String toString(A);
		};

		struct __A_VT {
			Class __isa;

			int32_t (*hashCode)(A);
			bool (*equals)(A, Object);
			Class (*getClass)(A);
			void (*setA)(A, String);
			void (*printOther)(A, A);
			String (*toString)(A);

			__A_VT()
			: __isa(__A::__class()),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			setA(&__A::setA),
			printOther(&__A::printOther),
			toString(&__A::toString)
			{}
		};

		struct __B1 {
			__B1_VT* __vptr;
			String b;
			__A parent;

			__B1();

			static Class __class();

			static __B1_VT __vtable;

		};

		struct __B1_VT {
			Class __isa;

			int32_t (*hashCode)(B1);
			bool (*equals)(B1, Object);
			Class (*getClass)(B1);
			void (*setA)(B1, String);
			void (*printOther)(B1, A);
			String (*toString)(B1);

			__B1_VT()
			: __isa(__B1::__class()),
			hashCode((int32_t(*)(B1))&__Object::hashCode),
			equals((bool(*)(B1, Object))&__Object::equals),
			getClass((Class(*)(B1))&__Object::getClass),
			setA((void(*)(B1, String))&__A::setA),
			printOther((void(*)(B1, A))&__A::printOther),
			toString((String(*)(B1))&__A::toString)
			{}
		};

		struct __B2 {
			__B2_VT* __vptr;
			String b;
			__A parent;

			__B2();

			static Class __class();

			static __B2_VT __vtable;

		};

		struct __B2_VT {
			Class __isa;

			int32_t (*hashCode)(B2);
			bool (*equals)(B2, Object);
			Class (*getClass)(B2);
			void (*setA)(B2, String);
			void (*printOther)(B2, A);
			String (*toString)(B2);

			__B2_VT()
			: __isa(__B2::__class()),
			hashCode((int32_t(*)(B2))&__Object::hashCode),
			equals((bool(*)(B2, Object))&__Object::equals),
			getClass((Class(*)(B2))&__Object::getClass),
			setA((void(*)(B2, String))&__A::setA),
			printOther((void(*)(B2, A))&__A::printOther),
			toString((String(*)(B2))&__A::toString)
			{}
		};

		struct __C {
			__C_VT* __vptr;
			String c;
			__B1 parent;

			__C();

			static Class __class();

			static __C_VT __vtable;

		};

		struct __C_VT {
			Class __isa;

			int32_t (*hashCode)(C);
			bool (*equals)(C, Object);
			Class (*getClass)(C);
			void (*setA)(C, String);
			void (*printOther)(C, A);
			String (*toString)(C);

			__C_VT()
			: __isa(__C::__class()),
			hashCode((int32_t(*)(C))&__Object::hashCode),
			equals((bool(*)(C, Object))&__Object::equals),
			getClass((Class(*)(C))&__Object::getClass),
			setA((void(*)(C, String))&__A::setA),
			printOther((void(*)(C, A))&__A::printOther),
			toString((String(*)(C))&__A::toString)
			{}
		};

	}
}
